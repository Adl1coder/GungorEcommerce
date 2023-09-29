package com.adilegungor.gungorecommerce.ui.favori

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adilegungor.gungorecommerce.common.loadImage
import com.adilegungor.gungorecommerce.data.model.ProductUI
import com.adilegungor.gungorecommerce.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val favProductListener: FavProductListener
) : ListAdapter<ProductUI, FavoriteAdapter.FavProductViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavProductViewHolder =
        FavProductViewHolder(
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            favProductListener
        )

    override fun onBindViewHolder(holder: FavProductViewHolder, position: Int) = holder.bind(getItem(position))

    class FavProductViewHolder(
        private val binding: ItemFavoriteBinding,
        private val favProductListener: FavProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            tvTitle.text = product.title
            tvPrice.text = "${product.price} TL"

            ivProduct.loadImage(product.imageOne)

            root.setOnClickListener {
                favProductListener.onProductClick(product.id)
            }

            ivDelete.setOnClickListener {
                favProductListener.onDeleteClick(product)
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

    interface FavProductListener {
        fun onProductClick(id: Int)
        fun onDeleteClick(product: ProductUI)
    }
}