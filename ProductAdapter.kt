package com.example.demo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.demo.R
import com.example.demo.databinding.ProductGridRecyclerLayoutBinding
import com.example.demo.databinding.ProductListRecyclerLayoutBinding
import com.example.demo.model.ProductItem
import com.example.demo.response.GetProductResponse
import com.example.demo.view.MainActivity

class ProductAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var recyclerListener: RecyclerListener

    var productList: ArrayList<GetProductResponse.GetProductResponseItem> = ArrayList()
    private var viewType = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) ListViewHolder(ProductListRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else GridViewHolder(ProductGridRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val product = productList[holder.adapterPosition]

        if (getItemViewType(viewType) == 0) {
            val myHolder = holder as ListViewHolder
            myHolder.bind(product, myHolder)
        } else {
            val myHolder = holder as GridViewHolder
            myHolder.bind(product, myHolder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    inner class ListViewHolder(private val binding: ProductListRecyclerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GetProductResponse.GetProductResponseItem, holder: ListViewHolder) {

            holder.binding.productImage.load(item.image ?: "") {
                crossfade(true)
                error(R.drawable.ic_empty_image)
            }
            holder.binding.category.text = item.category ?: ""
            holder.binding.title.text = item.title ?: ""
            holder.binding.ratingBar.rating = (item.rating?.rate ?: 0.0).toFloat()
            holder.binding.ratingRate.text = (item.rating?.rate ?: 0.0).toString()
            holder.binding.ratingCount.text = buildString {
                append("(")
                append((item.rating?.count ?: 0.0).toString())
                append(")")
            }
            holder.binding.price.text = (item.price ?: "0.0").toString()
            holder.binding.favourite.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context, if (item.isFavourite == 1) R.drawable.ic_favourite_filled else R.drawable.ic_favorite_border
                )
            )

            holder.binding.favourite.setOnClickListener {
                recyclerListener.onclick(holder.adapterPosition, item, if (item.isFavourite == 1) "remove" else "add")
            }
            holder.itemView.setOnClickListener {
                recyclerListener.onclick(holder.adapterPosition, item, "view")
            }
        }
    }

    inner class GridViewHolder(private val binding: ProductGridRecyclerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GetProductResponse.GetProductResponseItem, holder: GridViewHolder) {
            holder.binding.productImage.load(item.image ?: "") {
                crossfade(true)
                error(R.drawable.ic_empty_image)
            }
            holder.binding.category.text = item.category ?: ""
            holder.binding.title.text = item.title ?: ""
            holder.binding.ratingBar.rating = (item.rating?.rate ?: 0.0).toFloat()
            holder.binding.ratingRate.text = (item.rating?.rate ?: 0.0).toString()
            holder.binding.ratingCount.text = buildString {
                append("(")
                append((item.rating?.count ?: 0.0).toString())
                append(")")
            }
            holder.binding.price.text = (item.price ?: "0.0").toString()
            holder.binding.favourite.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context, if (item.isFavourite == 1) R.drawable.ic_favourite_filled else R.drawable.ic_favorite_border
                )
            )

            holder.binding.favourite.setOnClickListener {
                recyclerListener.onclick(holder.adapterPosition, item, if (item.isFavourite == 1) "remove" else "add")
            }
            holder.itemView.setOnClickListener {
                recyclerListener.onclick(holder.adapterPosition, item, "view")
            }
        }
    }


    fun updateList(list: ArrayList<GetProductResponse.GetProductResponseItem>) {
        productList = list
        notifyDataSetChanged()
    }

    fun updateViewType(newViewType: Int) {
        viewType = newViewType
        notifyDataSetChanged()
    }

    interface RecyclerListener {
        fun onclick(position: Int, item: GetProductResponse.GetProductResponseItem, clickType: String)
    }

    fun setRecyclerListener(recyclerListener: RecyclerListener) {
        this.recyclerListener = recyclerListener
    }

    fun updateFavourite(isFav: Int, p: Int) {
        productList[p].isFavourite = isFav
        notifyItemChanged(p)
    }

    fun removeFavourites(id: Int) {
        val index = productList?.indexOfFirst { it.id == id }
        index?.let {
            if (it != -1) {
                productList.removeAt(it)
                notifyItemRemoved(index)
            }

        }

    }

    fun addFavourite(productData: ProductItem) {
        productList.add(
            GetProductResponse.GetProductResponseItem(
                category = productData.category, description = productData.description, id = productData.id, image = productData.image, price = productData.price, rating = GetProductResponse.GetProductResponseItem.Rating(rate = productData.rate, count = productData.count), isFavourite = productData.isFavourite, title = productData.title
            )
        )
        notifyItemInserted(productList.size - 1)
    }

}
