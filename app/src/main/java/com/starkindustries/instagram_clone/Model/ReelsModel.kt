package com.starkindustries.instagram_clone.Model
import android.net.Uri
class ReelsModel
{
    lateinit var reelProfileImage:String
    lateinit var downlloadUrl:String
    lateinit var reelCaption:String
    lateinit var reelName:String
    constructor(reelName_:String,reelProfileImage_:String,downloadUrl_:String,reelCaption_:String)
    {
        this.reelName=reelName_
        this.reelProfileImage=reelProfileImage_
        this.downlloadUrl=downloadUrl_
        this.reelCaption=reelCaption_
    }
    constructor()
    {

    }
}