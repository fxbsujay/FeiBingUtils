package com.susu.utils;

import java.util.*;

/**
 * <p>Description: Implement the map interface and use the hash principle</p>
 * <p>实现Map接口并利用散列原理</p>
 * @author sujay
 * @version 21:59 2022/2/20
 * @see Map
 * @since JDK1.8
 */
public class SujayMap implements Map {

    public static void main(String[] args) {
        SujayMap hgcMap = new SujayMap();
        hgcMap.put(1,1);
        hgcMap.put("asd","211");
        Set entrySet = hgcMap.entrySet();
        System.out.println(hgcMap);
        Data entry = (Data) entrySet.iterator().next();
        entry.setKey("123123");
        System.out.println(entry);
    }

    private final static int SLOT = 997;

    private LinkedList[] bucket = new LinkedList[SLOT];

    int size = 0;

    class Data implements Entry, Comparable {
        Object key, value;

        public Data(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(Object o) {
            Data data = (Data) o;
            return ((Comparable) key).compareTo(data.key);
        }

        @Override
        public Object getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            Object result = this.value;
            this.value = value;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            Data data = (Data) obj;
            return this.key.equals(data.key);
        }

        @Override
        public String toString() {
            return "[" + key + ":" + value + "]";
        }

        public void setKey(Object key) {
            this.key = key;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean found = false;
        int index = key.hashCode() % SLOT;
        if(index < 0 ) {
            index= -index;
        }
        if(bucket[index]==null) {
            return found;
        }
        Iterator iterator = bucket[index].listIterator();
        while(iterator.hasNext()){
            if(iterator.next().equals((new Data(key,null)))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for(int i = 0 ; i < SLOT;i++){
            if(bucket[i]!=null){
                Iterator iterator = bucket[i].listIterator();
                while(iterator.hasNext()){
                    if(((Data)iterator.next()).getValue().equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        Data getData = new Data(key, null);
        int index = key.hashCode() % SLOT;
        if (index < 0) {
            index = -index;
        }
        if (bucket[index] == null) {
            return null;
        }
        LinkedList linkedList = bucket[index];
        ListIterator iterator = linkedList.listIterator();
        while (iterator.hasNext()) {
            Data data = (Data) iterator.next();
            if (data.equals(getData)) {
                return data.value;
            }
        }
        return null;
    }

    @Override
    public Object put(Object key, Object value) {
        Object result = new Object();
        Data putdata = new Data(key, value);
        int index = key.hashCode() % 997;
        if (index < 0) {
            index = -index;
        }
        if (bucket[index] == null) {
            bucket[index] = new LinkedList();
        }
        LinkedList linkedList = bucket[index];
        ListIterator iterator = linkedList.listIterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Data data = (Data) iterator.next();
            if (data.equals(putdata)) {
                found = true;
                result = data.value;
                iterator.set(putdata);
            }
        }
        if (!found) {
            bucket[index].add(putdata);
            size++;
        }
        return result;
    }

    @Override
    public Object remove(Object key) {
        Data removeData = new Data(key, null);
        int index = key.hashCode() % SLOT;
        if (index < 0) {
            index = -index;
        }
        if (bucket[index] == null) {
            return null;
        }
        LinkedList linkedList = bucket[index];
        size--;
        return linkedList.remove(removeData);
    }

    @Override
    public void putAll(Map m) {
        Set set = m.entrySet();
        for(Object o : set){
            Map.Entry oo = (Map.Entry)o;
            put(oo.getKey(),oo.getValue());
        }
    }

    @Override
    public void clear() {
        for(Object key : keySet()){
            remove(key);
        }
        size=0;
    }

    @Override
    public Set keySet() {
        Set set = new HashSet();
        for(int i = 0 ; i< SLOT ;i++){
            if(bucket[i]!=null){
                Iterator iterator = bucket[i].listIterator();
                while(iterator.hasNext()){
                    set.add(((Data)iterator.next()).getKey());
                }
            }
        }
        return set;
    }

    @Override
    public Collection values() {
        List list = new ArrayList();
        for(int i = 0 ; i< SLOT ;i++){
            if(bucket[i]!=null){
                Iterator iterator = bucket[i].listIterator();
                while(iterator.hasNext()){
                    list.add(((Data)iterator.next()).getValue());
                }
            }
        }
        return list;
    }

    @Override
    public Set<Entry> entrySet() {
        Set set = new HashSet();
        for(int i = 0 ; i< SLOT ;i++){
            if(bucket[i]!=null){
                Iterator iterator = bucket[i].listIterator();
                while(iterator.hasNext()){
                    set.add(iterator.next());
                }
            }
        }
        return set;
    }

    @Override
    public int hashCode() {
        int j = 0;
        for (int i = 0; i < SLOT; i++) {
            if (bucket[i] != null) {
                Iterator iterator = bucket[i].iterator();
                Data data = (Data) iterator.next();
                j = j + data.getKey().hashCode();
            }
        }
        return j;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append("\n");
        for(int i = 0 ; i < SLOT;i++){
            if(bucket[i]!=null){
                Iterator iterator = bucket[i].listIterator();
                while(iterator.hasNext()){
                    Data data = (Data)iterator.next();
                    builder.append(data.getKey()).append(":").append(data.getValue()).append("\n");
                }
            }
        }
        builder.append("]");
        return builder.toString();
    }

}
