package com.starkindustries.instagram_clone.Model
class Posts
{
    lateinit var postDownloadUrl:String
    lateinit var title:String
    lateinit var caption:String
    lateinit var uid:String
    lateinit var time:String
    constructor(postDownloadUrl_:String,caption_:String,title_:String,uid_:String,time_:String)
    {
        this.postDownloadUrl=postDownloadUrl_
        this.caption=caption_
        this.title=title_
        this.uid=uid_
        this.time=time_
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