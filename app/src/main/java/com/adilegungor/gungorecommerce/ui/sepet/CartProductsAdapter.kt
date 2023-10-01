package com.adilegungor.gungorecommerce.ui.sepet

import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adilegungor.gungorecommerce.R
import kotlinx.coroutines.launch
import com.adilegungor.gungorecommerce.common.loadImage
import com.adilegungor.gungorecommerce.data.model.ProductUI
import com.adilegungor.gungorecommerce.databinding.ItemCartProductBinding

class CartProductsAdapter(
    private val cartProductListener: CartProductListener,
    private val sharedPreferences: SharedPreferences
) : ListAdapter<ProductUI, CartProductsAdapter.CartProductViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder =
        CartProductViewHolder(
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            cartProductListener
        )

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) = holder.bind(getItem(position))

    inner class CartProductViewHolder(
        private val binding: ItemCartProductBinding,
        private val cartProductListener: CartProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentCount = 1
        private lateinit var sharedPreferencesKey: String

        fun bind(product: ProductUI) = with(binding) {
            tvTitle.text = product.title
            tvPrice.text = "${product.price} ₺"

            ivProduct.loadImage(product.imageOne)

            sharedPreferencesKey = "count_key_${product.id}"

            currentCount = sharedPreferences.getInt(sharedPreferencesKey, 1)
            tvCount.text = currentCount.toString()

            root.setOnClickListener {
                cartProductListener.onProductClick(product.id)
            }




            btnAdd.setOnClickListener {
                if (currentCount == product.count) {
                    tvCount.text = currentCount.toString()
                    cartProductListener.onIncreaseClick(product.price)
                } else {
                    currentCount += 1

                    tvCount.text = currentCount.toString()
                    val editor = sharedPreferences.edit()
                    editor.putInt(sharedPreferencesKey, currentCount)
                    editor.apply()
                }
            }

            btnMinus.setOnClickListener {
                if (currentCount >= 1) {
                    currentCount -= 1
                    cartProductListener.onDecreaseClick(product.price)
                } else {
                    cartProductListener.onDeleteClick(product.id, product.price)
                }

                tvCount.text = currentCount.toString()

                val editor = sharedPreferences.edit()
                editor.putInt(sharedPreferencesKey, currentCount)
                editor.apply()
            }

            ivDelete.setOnClickListener {
                cartProductListener.onDeleteClick(product.id, product.price)
                currentCount = 0
                val editor = sharedPreferences.edit()
                editor.putInt(sharedPreferencesKey, currentCount+1)
                editor.apply()
                tvCount.text = currentCount.toString()
            }
        }
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

    interface CartProductListener {
        fun onProductClick(id: Int)
        fun onDeleteClick(id: Int, price: Double)
        fun onIncreaseClick(price: Double)
        fun onDecreaseClick(price: Double)
    }
}