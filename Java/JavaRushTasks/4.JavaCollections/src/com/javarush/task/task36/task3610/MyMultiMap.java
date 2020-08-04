package com.javarush.task.task36.task3610;

import java.io.Serializable;
import java.util.*;

public class MyMultiMap<K, V> extends HashMap<K, V> implements Cloneable, Serializable {
    static final long serialVersionUID = 123456789L;
    private HashMap<K, List<V>> map;
    private int repeatCount;

    public MyMultiMap(int repeatCount) {
        this.repeatCount = repeatCount;
        map = new HashMap<>();
    }

    @Override
    public int size() {
        int i=0;
        for(Entry<K,List<V>> pair:map.entrySet())
            i+=pair.getValue().size();
        return i;
    }

    @Override
    public V put(K key, V value) {
        if(!map.containsKey(key)) {
            ArrayList<V> list=new ArrayList<V>();
            list.add(value);
            map.put(key, list );
            return null;
        }else{
            if(map.get(key).size()<repeatCount){
                map.get(key).add(value);
                if(map.get(key).size()<2) return null;
                    else return map.get(key).get(map.get(key).size()-2);}
            if(map.get(key).size()==repeatCount){map.get(key).remove(0); map.get(key).add(value);
                    return map.get(key).get(map.get(key).size()-2);}
            return null;
        }
    }

    @Override
    public V remove(Object key) {
        if (map.containsKey(key)) {
            if (map.get(key).size() == 0) {
                map.remove(key);
                return null;
            }
            if (map.get(key).size() > 1) {
                return map.get(key).remove(0);
            }
            if (map.get(key).size() == 1) {
                V val = map.get(key).remove(0);
                map.remove(key);
                return val;
            }
        }
        return null;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set=new HashSet<K>();
        for (Entry<K, List<V>> pair: map.entrySet())
            {set.add(pair.getKey());}
        return set;
    }

    @Override
    public Collection<V> values() {
        ArrayList<V> list=new ArrayList<V>();
        for (Entry<K, List<V>> pair: map.entrySet())
        {pair.getValue().stream().forEach(el->list.add(el));}
        return list;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for(Entry<K,List<V>> pair:map.entrySet())
            if(pair.getValue().contains(value)) return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            for (V v : entry.getValue()) {
                sb.append(v);
                sb.append(", ");
            }
        }
        String substring = sb.substring(0, sb.length() - 2);
        return substring + "}";
    }
}