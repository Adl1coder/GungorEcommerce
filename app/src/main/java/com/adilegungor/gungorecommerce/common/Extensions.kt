package com.adilegungor.gungorecommerce.common

import android.widget.ImageView
import com.bumptech.glide.Glide
import android.view.View

// Resim yükleme işlevi
fun ImageView.loadImage(url: String?) {
    Glide.with(this.context).load(url).into(this)
} 

// Görünürlüğü gizleme işlevi
fun View.gone() {
    visibility = View.GONE // View'ı görünmez yapar
}

// Görünürlüğü gösterme işlevi
fun View.visible() {
    visibility = View.VISIBLE // View'ı görünür yapar
    //buraya yeni eklemeler yapılabilir kod geliştirilebilir
}
