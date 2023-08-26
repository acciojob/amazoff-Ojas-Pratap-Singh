package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String , Order> orderDB = new HashMap<>(); // order id vs order object
    HashMap<String , DeliveryPartner> partnerDB = new HashMap<>(); // delivery partner id vs delivery partner object

    HashMap<String , List<String>> partenrOrderDB = new HashMap<>();// delivery partner id vs list of order id to be
                                                                        //delivered by this delivery person
    HashSet<String> orderNotAssignedDB = new HashSet<>(); //order not assinged


    public void addOrder(Order order) {
        orderDB.put(order.getId(),order);
        orderNotAssignedDB.add(order.getId());
    }

    public void addPartner(String partnerId) {
        partnerDB.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        List<String> list=partenrOrderDB.getOrDefault(partnerId,new ArrayList<>());
        list.add(orderId);
        partenrOrderDB.put(partnerId,list);
        partnerDB.get(partnerId).setNumberOfOrders(partnerDB.get(partnerId).getNumberOfOrders()+1);

        orderNotAssignedDB.remove(orderId);
    }

    public Order getOrderById(String orderId) {
        return orderDB.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerDB.get(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) { //7
        List<String> list = partenrOrderDB.getOrDefault(partnerId,new ArrayList<>());
        return list;
    }

    public Integer getOrderCountByPartnerId(String partnerId) { // 6
        if(partenrOrderDB.containsKey(partnerId) == false){
            return 0;
        }
        return partenrOrderDB.get(partnerId).size();
    }

    public List<String> getAllOrders() {
        List<String> list=new ArrayList<>();
        for(String s: orderDB.keySet()){
            list.add(s);
        }
        return list;
    }

    public Integer getCountOfUnassignedOrders() {
        return orderNotAssignedDB.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        Integer count=0;
        //converting given string time to integer
        String arr[]=time.split(":"); //12:45
        int hr=Integer.parseInt(arr[0]);
        int min=Integer.parseInt(arr[1]);

        int total=(hr*60+min);

        List<String> list=partenrOrderDB.getOrDefault(partnerId,new ArrayList<>());
        if(list.size()==0)return 0; //no order assigned to partnerId

        for(String s: list){
            Order currentOrder=orderDB.get(s);
            if(currentOrder.getDeliveryTime()>total){
                count++;
            }
        }

        return count;

    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        //return in HH:MM format
        String str="00:00";
        int max=0;

        List<String>list=partenrOrderDB.getOrDefault(partnerId,new ArrayList<>());
        if(list.size()==0)return str;
        for(String s: list){
            Order currentOrder=orderDB.get(s);
            max=Math.max(max,currentOrder.getDeliveryTime());
        }
        //convert int to string (140-> 02:20)
        int hr=max/60;
        int min=max%60;

        if(hr<10){
            str="0"+hr+":";
        }else{
            str=hr+":";
        }

        if(min<10){
            str+="0"+min;
        }
        else{
            str+=min;
        }
        return str;
    }

    public void deletePartnerById(String partnerId) {
        if(!partenrOrderDB.isEmpty()) {
            List<String> list = partenrOrderDB.get(partnerId);
            for (String s : list) {
                orderNotAssignedDB.add(s);

            }
        }
        partnerDB.remove(partnerId);
        partenrOrderDB.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        if(orderDB.containsKey(orderId)){
            if(orderNotAssignedDB.contains(orderId)){
                orderNotAssignedDB.remove(orderId);
            }
            else{

                for(String str : partenrOrderDB.keySet()){
                    List<String> list=partenrOrderDB.get(str);
                    list.remove(orderId);
                }
            }
        }

    }


}
