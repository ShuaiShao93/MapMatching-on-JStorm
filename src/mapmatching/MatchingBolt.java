package mapmatching;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import interfaces.MatchingType;
import factory.JythonObjectFactory;


public class MatchingBolt extends BaseRichBolt {
	MatchingType matching;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MatchingBolt(){
		JythonObjectFactory factory = new JythonObjectFactory(MatchingType.class, "Matching", "Matching");

		matching = (MatchingType)factory.createObject();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		String devicesn = tuple.getStringByField("devicesn");
		long timestamp = tuple.getLongByField("timestamp") ;
		double lon = tuple.getDoubleByField("lon");
		double lat = tuple.getDoubleByField("lat");
		double spd = tuple.getDoubleByField("spd");

	    matching.point_matching(devicesn, timestamp, lon, lat, spd);
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		// TODO Auto-generated method stub
	}
}
