package mapmatching;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class MatchingSpout extends BaseRichSpout {
	private SpoutOutputCollector _collector;

	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub
		String JsonContext = ReadFile("/root/mapmatching/project_jstorm/111.txt");
		JSONArray jsonArray = JSONArray.fromObject(JsonContext);
		int size = jsonArray.size();
		//System.out.println("Size: " + size);
		for(int  i = 0; i < size; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			_collector.emit(new Values(jsonObject.get("devicesn"), jsonObject.get("timestamp"), jsonObject.get("lon"), jsonObject.get("lat"), jsonObject.get("spd")));
		}
	}

	@Override
	public void open(Map arg0, TopologyContext topologyContext, SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		this._collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("devicesn", "timestamp", "lon", "lat", "spd"));
	}

	public String ReadFile(String path){
	    File file = new File(path);
	    BufferedReader reader = null;
	    String laststr = "";
	    try {
	     //System.out.println("以行为单位读取文件内容，一次读一整行：");
	     reader = new BufferedReader(new FileReader(file));
	     String tempString = null;
	     //一次读入一行，直到读入null为文件结束
	     while ((tempString = reader.readLine()) != null) {
	      //显示行号
	      laststr = laststr + tempString + ','; //由于轨迹文件格式不规范，需要在最后加上逗号才能正确识别为json格式
	     }
	     reader.close();
	    } catch (IOException e) {
	     e.printStackTrace();
	    } finally {
	     if (reader != null) {
	      try {
	       reader.close();
	      } catch (IOException e1) {
	      }
	     }
	    }
	    return laststr;
	}
}
