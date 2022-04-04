package dev.matyaqubov.firebaseinandroid.model

class Post {
    var id: String? = null
    var title: String? = null
    var body: String? = null
    var img: String = ""

    constructor()
    constructor(title: String?, body: String?) {
        this.title = title
        this.body = body
    }

    constructor(id: String?, title: String?, body: String?) {
        this.id = id
        this.title = title
        this.body = body
    }


}