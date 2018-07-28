package com.example.asus.summervacationproject.bean;


import java.util.List;

/**
 * Created by ASUS on 2018/7/21.
 */

public class ResultBeanData{

    private int code;
    private String msg;
    private ResultBean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }



    public static class ResultBean {

        //广告横幅类型
        private List<BannerInfoBean> banner_info;
        //主题页面类型
        private List<ThemeInfoBean> themeInfoBean;
        //品牌类型
        private List<BrandInfoBean> brandInfoBean;
        //折扣类型
        private List<DiscountInfoBean> discountInfoBean;
        //推荐类型
        private List<RecommendInfoBean> recommend_info;


        public List<BannerInfoBean> getBanner_info() {
            return banner_info;
        }

        public void setBanner_info(List<BannerInfoBean> banner_info) {
            this.banner_info = banner_info;
        }

        public List<ThemeInfoBean> getThemeInfoBean() {

            return themeInfoBean;
        }

        public void setThemeInfoBean(List<ThemeInfoBean> themeInfoBean) {
            this.themeInfoBean = themeInfoBean;
        }

        public List<BrandInfoBean> getBrandInfoBean() {
            return brandInfoBean;
        }

        public void setBrandInfoBean(List<BrandInfoBean> brandInfoBean) {
            this.brandInfoBean = brandInfoBean;
        }

        public List<DiscountInfoBean> getDiscountInfoBean() {
            return discountInfoBean;
        }

        public void setDiscountInfoBean(List<DiscountInfoBean> discountInfoBean) {
            this.discountInfoBean = discountInfoBean;
        }

        public List<RecommendInfoBean> getRecommend_info() {
            return recommend_info;
        }


        public void setRecommend_info(List<RecommendInfoBean> recommend_info) {
            this.recommend_info = recommend_info;
        }


        public static class BannerInfoBean {
            private String image;
            private int option;
            private int type;
            private ValueBean valueBean;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getOption() {
                return option;
            }

            public void setOption(int option) {
                this.option = option;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public ValueBean getValueBean() {
                return valueBean;
            }

            public void setValueBean(ValueBean valueBean) {
                this.valueBean = valueBean;
            }

            @Override
            public String toString() {
                return "BannerInfoBean{" +
                        "image='" + image + '\'' +
                        ", option=" + option +
                        ", type=" + type +
                        ", valueBean=" + valueBean +
                        '}';
            }

            public static class ValueBean {
                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

        }


        public static class ThemeInfoBean {
            private String name;
            private String url;
            private String image;
            private int id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            @Override
            public String toString() {
                return "ThemeInfoBean{" +
                        "image='" + image + '\'' +
                        ", name=" + name +
                        ", image=" + image +
                        ", id=" + id +
                        '}';
             }
        }




        public static class BrandInfoBean {
            private String image;
            private String url;
            private int id;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

        }


        public static class DiscountInfoBean {
            private String name;
            private String image;
            private int id;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }


        public static class RecommendInfoBean {
            private int id;
            private String name;
            private int cover_price;
            private String image;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCover_price() {
                return cover_price;
            }

            public void setCover_price(int cover_price) {
                this.cover_price = cover_price;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}