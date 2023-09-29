package com.adilegungor.gungorecommerce.ui.anasayfa

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adilegungor.gungorecommerce.common.loadImage
import com.adilegungor.gungorecommerce.data.model.ProductUI
import com.adilegungor.gungorecommerce.databinding.ItemProductBinding

class ProductAdapter (
    private val productListener: ProductListener
) : ListAdapter<ProductUI, ProductAdapter.ProductViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productListener
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ProductViewHolder(
        private val binding: ItemProductBinding,
        private val productListener: ProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            tvTitle.text = product.title
            tvDesc.text = product.description
            tvCategory.text = product.category

            imgProduct.loadImage(product.imageOne)

            if (product.saleState == true) {
                tvSalePrice.isVisible = true
                tvSalePrice.text = "${product.salePrice} ₺"
                tvPrice.text = "${product.price} TL"
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvPrice.text = "${product.price} TL"
                tvSalePrice.isVisible = false
            }

            imgFavorite.setOnClickListener {
                productListener.onFavoriteClick(product)
            }

            root.setOnClickListener {
                productListener.onProductClick(product.id)
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

    interface ProductListener {
        fun onProductClick(id: Int)
        fun onFavoriteClick(product: ProductUI)
    }

}