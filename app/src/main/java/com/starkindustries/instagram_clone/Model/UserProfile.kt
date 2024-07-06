package com.starkindustries.instagram_clone.Model
class UserProfile
{
    lateinit var name:String
    lateinit var email:String
    lateinit var phoneNo:String
    lateinit var username:String
    lateinit var password:String
    lateinit var photoUri:String
    lateinit var signintype:String
    constructor(name_:String,email_:String,phoneNo_:String,username_:String,password_:String,photoUri_:String,signintype_:String)
    {
        this.name=name_
        this.email=email_
        this.phoneNo=phoneNo_
        this.username=username_
        this.password=password_
        this.photoUri=photoUri_
        this.signintype=signintype_
    }
    constructor(name_:String,email_:String,phoneNo_:String,username_:String)
    {
        this.name=name_
        this.email=email_
        this.phoneNo=phoneNo_
        this.username=username_
    }
    constructor(name_:String,email_:String,phoneNo_:String,username_:String,password_:String)
    {
        this.name=name_
        this.email=email_
        this.phoneNo=phoneNo_
        this.username=username_
        this.password=password_
    }
    constructor()
    {

    }
}