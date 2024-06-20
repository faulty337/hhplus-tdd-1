package io.hhplus.tdd;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConcurrentManager {
    private final ConcurrentHashMap<Long, Boolean> hashMap = new ConcurrentHashMap<>();


    public boolean isOperation(long key){
        return hashMap.getOrDefault(key, false);
    }

    public void startOperation(long key){
        hashMap.put(key, true);
    }

    public void endOperation(long key){
        hashMap.remove(key);
    }

    public boolean waitOperation(long key, long millis)  {
        for(int i = 0; i < 10; i++){
            if(isOperation(key)){
                try {
                    Thread.sleep(millis/10);
                }catch(InterruptedException e) {
                    throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
            }else{
                return false;
            }
        }
        return true;
    }
}
