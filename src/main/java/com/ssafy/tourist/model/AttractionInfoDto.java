package com.ssafy.tourist.model;

public class AttractionInfoDto {
    private int contentId;
    private int contentTypeId;
    private String title;
    private String addr1;
    private String addr2;
    private String zipcode;
    private String tel;
    private String first_image;
    private String first_image2;
    private int readcount;
    private int sidoCode;
    private int gugunCode;
    private double latitude;
    private double longitude;
    private String mlevel;

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(int contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFirst_image() {
        return first_image;
    }

    public void setFirst_image(String first_image) {
        this.first_image = first_image;
    }

    public String getFirst_image2() {
        return first_image2;
    }

    public void setFirst_image2(String first_image2) {
        this.first_image2 = first_image2;
    }

    public int getReadcount() {
        return readcount;
    }

    public void setReadcount(int readcount) {
        this.readcount = readcount;
    }

    public int getSidoCode() {
        return sidoCode;
    }

    public void setSidoCode(int sidoCode) {
        this.sidoCode = sidoCode;
    }

    public int getGugunCode() {
        return gugunCode;
    }

    public void setGugunCode(int gugunCode) {
        this.gugunCode = gugunCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMlevel() {
        return mlevel;
    }

    public void setMlevel(String mlevel) {
        this.mlevel = mlevel;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("contentId=").append(contentId);
        sb.append(", contentTypeId=").append(contentTypeId);
        sb.append(", title='").append(title).append('\'');
        sb.append(", addr1='").append(addr1).append('\'');
        sb.append(", addr2='").append(addr2).append('\'');
        sb.append(", zipcode='").append(zipcode).append('\'');
        sb.append(", tel='").append(tel).append('\'');
        sb.append(", first_image='").append(first_image).append('\'');
        sb.append(", first_image2='").append(first_image2).append('\'');
        sb.append(", readcount=").append(readcount);
        sb.append(", sidoCode=").append(sidoCode);
        sb.append(", gugunCode=").append(gugunCode);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", mlevel='").append(mlevel).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
