package com.adilegungor.gungorecommerce.common

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// AppCompatActivity için bir extension fonksiyonu.
// Verilen bir LayoutInflater işlevi kullanarak ViewBinding oluşturur.
inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline factory: (LayoutInflater) -> T
//lazy: viewbindng nesnesini  sadece  ilk kez ihtiyaç olunca oluştur.
) = lazy(LazyThreadSafetyMode.NONE) {  // bu işlemi tek bir iş parçacığında gerçekleştir
    factory(layoutInflater)
}

// Fragment için bir extension fonksiyonu. ViewBinding'i oluştururken bir view oluşturucu işlevini kullanır.
fun <T : ViewBinding> Fragment.viewBinding(factory: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

        private var binding: T? = null

        // ViewBinding'i temsil eden değeri döndürür. Eğer henüz oluşturulmamışsa oluşturur.
        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            binding ?: factory(requireView()).also {
                if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                    // ViewLifecycleOwner'ın yaşam döngüsünü izler ve bağlantıyı korur.
                    viewLifecycleOwner.lifecycle.addObserver(this)
                    binding = it
                }
            }

        // Fragment öldüğünde, ViewBinding'i temizler.
        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }
    }
