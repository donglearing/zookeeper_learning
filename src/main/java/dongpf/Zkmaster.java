package dongpf;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;

/**
 * Created by pengfei.dong
 * Date 2018/8/8
 * Time 下午7:50
 */

public class Zkmaster implements Watcher {

    private ZooKeeper zk;

    private String hoststr ;

    private Random random = new Random(this.hashCode());

    private String serverId = Integer.toHexString(random.nextInt());

    public static AsyncCallback.StringCallback StrCallBack = new AsyncCallback.StringCallback(){
       public  void processResult(int rc, String path, Object ctx, String name){

           System.out.println(path);
           switch (rc){

           }

        }
    };

    Watcher newTaskWatcher = new Watcher(){
        public void process(WatchedEvent e) {
            System.out.println("监控有所改变--->" + e.toString());
        }
    };

    @Override
    public void process(WatchedEvent watchedEvent) {

        System.out.println("dongpf1--->>" + watchedEvent.toString());

    }

    Zkmaster(String hoststr){
        this.hoststr = hoststr;
    }

    public void startZk() throws IOException {
        this.zk = new ZooKeeper(hoststr,15000,this);
    }

    public void stopZk() throws InterruptedException {
        this.zk.close();
    }

    public void runForMaster() throws KeeperException, InterruptedException{
        zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
    }

    public void getFormMasterData() throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        zk.getData("/master", newTaskWatcher, stat);
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        Zkmaster zkmaster = new Zkmaster("127.0.0.1:2181");
        zkmaster.startZk();
        zkmaster.runForMaster();
        zkmaster.getFormMasterData();
        Thread.sleep(1000 * 60 * 30);
    }




}
