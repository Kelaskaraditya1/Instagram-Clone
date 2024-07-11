package com.starkindustries.instagram_clone.Model
class UserPost
{
    lateinit var postDownloadUrl:String
    lateinit var profileImageDownloadUrl:String
    lateinit var name:String
    lateinit var caption:String
    lateinit var time:String
    lateinit var title:String
    constructor(profileImageDownloadUrl_:String,postDownloadUrl_:String,caption_:String,name_:String,title_:String,time_:String)
    {
        this.profileImageDownloadUrl=profileImageDownloadUrl_
        this.postDownloadUrl=postDownloadUrl_
        this.caption=caption_
        this.name=name_
        this.title=title_
        this.time=time_
    }
    constructor(postDownloadUrl_:String,caption_:String,name_:String,time_:String)
    {
        this.postDownloadUrl=postDownloadUrl_
        this.caption=caption_
        this.name=name_
        this.time=time_
    }
    constructor(caption_:String,name_:String)
    {
        this.caption=caption_
        this.name=name_
    }
    constructor()
    {

    }
}