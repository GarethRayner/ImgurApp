package uk.co.pagesuite.imgurapp;

/*
    My custom object class for storing suitable data fetched from the JSON data. This permits easy transport and less memory overhead.
 */
public class Sub {
    /*
        Initialise global variables.
            id              The id of this particular post.
            title           The title of this particular post.
            imageUrl        The image URL of this particular post.
            imageThumbUrl   The thumbnail image of this particular post. (Currently unused)
     */
    public String id;
    public String title;
    public String imageUrl;
    public String imageThumbUrl;
    public String url;
    public int upvotes;

    /*
        Simple constructor for storing the variables passed.
            @params
                i   The id to be stored for this post.
                t   The title to be stored for this post.
                iM  The image URL to be stored for this post.
                up  The upvotes for this post.
     */
    public Sub(String i, String t, String iM, int up, String u) {
        //Simply set all the variables with appropriate param.
        id = i;
        title = t;
        imageUrl = iM;
        url = u;
        upvotes = up;
    }
}
