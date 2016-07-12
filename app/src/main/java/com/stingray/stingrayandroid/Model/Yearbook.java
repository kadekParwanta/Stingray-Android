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
    private String coverUrl;
    @SerializedName("epub_url")
    @Expose
    private String epubUrl;
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
        return coverUrl;
    }

    /**
     *
     * @param coverUrl
     * The cover_url
     */
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    /**
     *
     * @return
     * The epubUrl
     */
    public String getEpubUrl() {
        return epubUrl;
    }

    /**
     *
     * @param epubUrl
     * The epub_url
     */
    public void setEpubUrl(String epubUrl) {
        this.epubUrl = epubUrl;
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
