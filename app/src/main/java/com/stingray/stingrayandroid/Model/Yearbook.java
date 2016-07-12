package com.stingray.stingrayandroid.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.strongloop.android.loopback.Model;

/**
 * Created by Kadek_P on 7/12/2016.
 */
public class Yearbook extends Model {
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("school")
    @Expose
    private School school;
    @SerializedName("cover_url")
    @Expose
    private String cover_url;
    @SerializedName("epub_url")
    @Expose
    private String epub_url;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     *
     * @return
     * The year
     */
    public Integer getYear() {
        return year;
    }

    /**
     *
     * @param year
     * The year
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     *
     * @return
     * The school
     */
    public School getSchool() {
        return school;
    }

    /**
     *
     * @param school
     * The school
     */
    public void setSchool(School school) {
        this.school = school;
    }

    /**
     *
     * @return
     * The coverUrl
     */
    public String getCoverUrl() {
        return cover_url;
    }

    /**
     *
     * @param coverUrl
     * The cover_url
     */
    public void setCoverUrl(String coverUrl) {
        this.cover_url = coverUrl;
    }

    /**
     *
     * @return
     * The epubUrl
     */
    public String getEpubUrl() {
        return epub_url;
    }

    /**
     *
     * @param epubUrl
     * The epub_url
     */
    public void setEpubUrl(String epubUrl) {
        this.epub_url = epubUrl;
    }

    /**
     *
     * @return
     * The price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }
}
