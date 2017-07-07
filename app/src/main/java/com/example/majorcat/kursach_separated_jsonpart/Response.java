package com.example.majorcat.kursach_separated_jsonpart;

import java.util.List;

/**
 * Created by nikolay on 23.04.17.
 */

public class Response {


    private List<ObjectsBean> objects;

    public List<ObjectsBean> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectsBean> objects) {
        this.objects = objects;
    }

    public static class ObjectsBean {
        /**
         * id : 1
         * adres : г.Москва, ул.Армавирская, д.17
         * appartament : 97
         * bywhom : Литвинов Иван Аркадьевич
         * senderphone : +7 495 4723003
         * recipientphone : +7 495 4723003
         * towhom : Литвинов Иван Аркадьевич
         * good : Тима 185 см (кремовый)
         * price : 4990
         * description :
         * isdelivered : 0
         */

        private int id;
        private String adres;
        private int appartament;
        private String bywhom;
        private String senderphone;
        private String recipientphone;
        private String towhom;
        private String good;
        private int price;
        private String description;
        private String isdelivered;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAdres() {
            return adres;
        }

        public void setAdres(String adres) {
            this.adres = adres;
        }

        public int getAppartament() {
            return appartament;
        }

        public void setAppartament(int appartament) {
            this.appartament = appartament;
        }

        public String getBywhom() {
            return bywhom;
        }

        public void setBywhom(String bywhom) {
            this.bywhom = bywhom;
        }

        public String getSenderphone() {
            return senderphone;
        }

        public void setSenderphone(String senderphone) {
            this.senderphone = senderphone;
        }

        public String getRecipientphone() {
            return recipientphone;
        }

        public void setRecipientphone(String recipientphone) {
            this.recipientphone = recipientphone;
        }

        public String getTowhom() {
            return towhom;
        }

        public void setTowhom(String towhom) {
            this.towhom = towhom;
        }

        public String getGood() {
            return good;
        }

        public void setGood(String good) {
            this.good = good;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIsdelivered() {
            return isdelivered;
        }

        public void setIsdelivered(String isdelivered) {
            this.isdelivered = isdelivered;
        }
    }
}
