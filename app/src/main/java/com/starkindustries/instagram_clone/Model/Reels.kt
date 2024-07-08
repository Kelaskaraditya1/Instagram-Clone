package com.starkindustries.instagram_clone.Model
class Reels
{
    lateinit var reelDownloadUrl:String
    lateinit var title:String
    lateinit var caption:String
    constructor(reelDownloadUrl_:String,caption_:String,title_:String)
    {
        this.reelDownloadUrl=reelDownloadUrl_
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