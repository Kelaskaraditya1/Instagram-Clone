package com.starkindustries.instagram_clone.Model
class Posts
{
    lateinit var postDownloadUrl:String
    lateinit var title:String
    lateinit var caption:String
    constructor(postDownloadUrl_:String,caption_:String,title_:String)
    {
        this.postDownloadUrl=postDownloadUrl_
        this.caption=caption_
        this.title=title_
    }
    constructor(caption_:String,title_:String)
    {
        this.caption=caption_
        this.title=title_
    }
    constructor()
    {

    }
}