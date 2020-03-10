package data.data;
import com.stc.common.config.BaseConfigReader;
import java.util.Map;
import java.util.LinkedHashMap;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.stc.worldserver.define.DataChecks;

@Service
public class Configs extends BaseConfigReader{
    
    private Map<Integer,DataFruitMachine> _DataFruitMachine;
    public Map<Integer, DataFruitMachine> getDataFruitMachine(){
        return _DataFruitMachine;
    }
    private Map<Integer,DataGrading> _DataGrading;
    public Map<Integer, DataGrading> getDataGrading(){
        return _DataGrading;
    }
    private Map<Integer,DataShortCut> _DataShortCut;
    public Map<Integer, DataShortCut> getDataShortCut(){
        return _DataShortCut;
    }
    private Map<Integer,DataShortCut2> _DataShortCut2;
    public Map<Integer, DataShortCut2> getDataShortCut2(){
        return _DataShortCut2;
    }
    private Map<Integer,DataShortCut3> _DataShortCut3;
    public Map<Integer, DataShortCut3> getDataShortCut3(){
        return _DataShortCut3;
    }
    private Map<Integer,DataVip> _DataVip;
    public Map<Integer, DataVip> getDataVip(){
        return _DataVip;
    }
    private Map<Integer,DataSign> _DataSign;
    public Map<Integer, DataSign> getDataSign(){
        return _DataSign;
    }
    private Map<Integer,DataVipSign> _DataVipSign;
    public Map<Integer, DataVipSign> getDataVipSign(){
        return _DataVipSign;
    }
    private Map<Integer,DataChouJiang> _DataChouJiang;
    public Map<Integer, DataChouJiang> getDataChouJiang(){
        return _DataChouJiang;
    }
    private Map<Integer,DataOnLineReward> _DataOnLineReward;
    public Map<Integer, DataOnLineReward> getDataOnLineReward(){
        return _DataOnLineReward;
    }
    private Map<Integer,DataTask> _DataTask;
    public Map<Integer, DataTask> getDataTask(){
        return _DataTask;
    }

    @Override
    @PostConstruct
    public void init() {
        _DataFruitMachine = JSON.parseObject(loadJson("DataFruitMachine"), new TypeReference<LinkedHashMap<Integer, DataFruitMachine>>(){});
 DataChecks.checkDataFruitMachine(_DataFruitMachine);
_DataGrading = JSON.parseObject(loadJson("DataGrading"), new TypeReference<LinkedHashMap<Integer, DataGrading>>(){});
 DataChecks.checkDataGrading(_DataGrading);
_DataShortCut = JSON.parseObject(loadJson("DataShortCut"), new TypeReference<LinkedHashMap<Integer, DataShortCut>>(){});
 DataChecks.checkDataShortCut(_DataShortCut);
_DataShortCut2 = JSON.parseObject(loadJson("DataShortCut2"), new TypeReference<LinkedHashMap<Integer, DataShortCut2>>(){});
 DataChecks.checkDataShortCut2(_DataShortCut2);
_DataShortCut3 = JSON.parseObject(loadJson("DataShortCut3"), new TypeReference<LinkedHashMap<Integer, DataShortCut3>>(){});
 DataChecks.checkDataShortCut3(_DataShortCut3);
_DataVip = JSON.parseObject(loadJson("DataVip"), new TypeReference<LinkedHashMap<Integer, DataVip>>(){});
 DataChecks.checkDataVip(_DataVip);
_DataSign = JSON.parseObject(loadJson("DataSign"), new TypeReference<LinkedHashMap<Integer, DataSign>>(){});
 DataChecks.checkDataSign(_DataSign);
_DataVipSign = JSON.parseObject(loadJson("DataVipSign"), new TypeReference<LinkedHashMap<Integer, DataVipSign>>(){});
 DataChecks.checkDataVipSign(_DataVipSign);
_DataChouJiang = JSON.parseObject(loadJson("DataChouJiang"), new TypeReference<LinkedHashMap<Integer, DataChouJiang>>(){});
 DataChecks.checkDataChouJiang(_DataChouJiang);
_DataOnLineReward = JSON.parseObject(loadJson("DataOnLineReward"), new TypeReference<LinkedHashMap<Integer, DataOnLineReward>>(){});
 DataChecks.checkDataOnLineReward(_DataOnLineReward);
_DataTask = JSON.parseObject(loadJson("DataTask"), new TypeReference<LinkedHashMap<Integer, DataTask>>(){});
 DataChecks.checkDataTask(_DataTask);

    }
}
