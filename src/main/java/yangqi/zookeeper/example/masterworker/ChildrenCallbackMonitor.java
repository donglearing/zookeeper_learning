/*
 * Copyright 1999-2010 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package yangqi.zookeeper.example.masterworker;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ChildrenCallbackMonitor {

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        Watcher wa =  new Watcher(){
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event);
            }
        };
        final ZooKeeper zookeeper = new ZooKeeper("localhost:2181", 2000, wa);

        final ChildrenCallback callback = new ChildrenCallback() {

            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children) {
                System.out.println(children);

            }

        };

        AsyncCallback.DataCallback data = new AsyncCallback.DataCallback(){
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println(new String(data));
            }
        };

        Watcher watcher = new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                System.out.println("Event is " + event);
                if (event.getType() == EventType.NodeChildrenChanged) {
                    System.out.println("Changed " + event);
                    zookeeper.getChildren("/workers", this, callback, null);
                }
            }
        };

        Watcher dataWatch = new Watcher(){
              @Override
              public void process(  WatchedEvent event){
                  System.out.println(event.getType());
                  if(event.getType() == EventType.NodeDataChanged){
                      System.out.println("Changed " + event);
                      zookeeper.getData("/workers/dongpf", this, data, null);
                  }
              }
        };
//        System.out.
        zookeeper.getChildren("/workers", watcher, callback, null);
        zookeeper.getData("/workers/dongpf", dataWatch, data, null);
        System.out.println("begin finish");
        Thread.sleep(200000);
        System.out.println("finish");

    }

}
