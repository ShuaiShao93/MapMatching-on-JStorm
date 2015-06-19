package mapmatching;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.SpoutDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.alibaba.jstorm.utils.JStormUtils;

public class MatchingTopology {
	private static Map conf = new HashMap<Object, Object>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TopologyBuilder builder = new TopologyBuilder();
		//创建topology的生成器

		int spoutParal = JStormUtils.parseInt(conf.get("spout.parallel"), 1);
		//获取spout的并发设置

		SpoutDeclarer spout = builder.setSpout("MatchingSpout", new MatchingSpout(), spoutParal);
		//创建Spout， 其中new SequenceSpout() 为真正spout对象，SequenceTopologyDef.SEQUENCE_SPOUT_NAME 为spout的名字，注意名字中不要含有空格

		int boltParal = JStormUtils.parseInt(conf.get("bolt.parallel"), 1);
		//获取bolt的并发设置

		BoltDeclarer totalBolt = builder.setBolt("MatchingBolt", new MatchingBolt(), boltParal).fieldsGrouping("MatchingSpout", new Fields("devicesn"));
		//创建bolt， SequenceTopologyDef.TOTAL_BOLT_NAME 为bolt名字，TotalCount 为bolt对象，boltParal为bolt并发数，
		//shuffleGrouping（SequenceTopologyDef.SEQUENCE_SPOUT_NAME）， 
		//表示接收SequenceTopologyDef.SEQUENCE_SPOUT_NAME的数据，并且以shuffle方式，
		//即每个spout随机轮询发送tuple到下一级bolt中

		int ackerParal = JStormUtils.parseInt(conf.get("acker.parallel"), 1);
		Config.setNumAckers(conf, ackerParal);
		//设置表示acker的并发数

		int workerNum = JStormUtils.parseInt(conf.get("worker.num"), 10);
		Config.setNumWorkers(conf, workerNum);
		//表示整个topology将使用几个worker

		conf.put(Config.NIMBUS_HOST, "localhost");

		try {
			StormSubmitter.submitTopology("MatchingTopology", conf, builder.createTopology());
		} catch (AlreadyAliveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//提交topology

	}

}
