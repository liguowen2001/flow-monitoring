package plus.liguowen.api.controller;

import plus.liguowen.api.Environment;
import plus.liguowen.api.entity.Log;
import plus.liguowen.api.service.DingService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Flow {
    static String fileRoute = Environment.logRoute;

    public static void main(){
        // run in a second
        final long timeInterval = 1000*60*60*24;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String flow = statisticalFlow();
                        String message = "今日使用"+flow+"M流量";
                        System.out.println(message);
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
         try {
             reader = new BufferedReader(new FileReader(fileRoute));
             String line = reader.readLine();

             analyse(line);
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
         double flow = 0;
         for (Log log : logs
         ) {
             flow+=log.getSentByte();
         }
         flow = flow/1024/1024;
         return String.format("%.2f",flow);
    }

    /**
     * 日志字符串分解转为Log对象
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
