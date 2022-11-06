import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Flow {
    static String fileRoute = Environment.logRoute;

    public static void main() {
        // 24小时执行一次
        final long timeInterval = 1000 * 60 * 60 * 24;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String flow = statisticalFlow();
                        String message = "test 今日使用" + flow + "M流量";
                        DingService.dingRequest(message);
                        Thread.sleep(timeInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * 统计流量
     */
    public static String statisticalFlow() {
        BufferedReader reader;
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<Log> logs = new ArrayList<>();

        File file = new File(fileRoute);
        if (!file.exists()){
            System.out.println("文件不存在");
            return "0";
        }

        // 读取文件
        try {
            reader = new BufferedReader(new FileReader(fileRoute));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            for (String str : lines
            ) {
                logs.add(analyse(str));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 计算流量
        double flow = 0;
        for (Log log : logs
        ) {
            flow += log.getSentByte();
        }
        flow = flow / 1024 / 1024;
        return String.format("%.2f", flow);
    }

    /**
     * 日志字符串分解转为Log对象
     *
     * @param str 输入
     * @return Log
     */
    private static Log analyse(String str) {
        String deliMeter = " ";
        String[] temp;
        temp = str.split(deliMeter);
        Log log = new Log();
        if (temp.length > 3) {
            log.setTime(Double.parseDouble(temp[1]));
            log.setSentByte(Integer.parseInt(temp[2]));
        }
        return log;
    }
}
