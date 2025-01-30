package com.example.demo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.demo.R
import com.example.demo.adapter.ProductAdapter
import com.example.demo.databinding.ActivityFavouritesBinding
import com.example.demo.model.ProductItem
import com.example.demo.response.GetProductResponse


class FavouritesActivity : AppCompatActivity() {
    private lateinit var favouritesBinding: ActivityFavouritesBinding
    private var viewType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouritesBinding = DataBindingUtil.setContentView(this, R.layout.activity_favourites)
        favouritesBinding.actionBar.back.visibility = View.VISIBLE
        favouritesBinding.actionBar.back.setOnClickListener {
            finish()
        }
        favouritesBinding.actionBar.title.text = getString(R.string.favourites)
        favouritesBinding.toggleButton.setOnCheckedChangeListener { _, isChecked ->
            viewType = if (isChecked) {
                1
            } else {
                0
            }
            setLayoutManager()
            productAdapter?.updateViewType(viewType)
        }
        setLayoutManager()
        productAdapter = ProductAdapter()
        favouritesBinding.productRecycler.adapter = productAdapter
        (favouritesBinding.productRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        productAdapter?.setRecyclerListener(object : ProductAdapter.RecyclerListener {
            override fun onclick(position: Int, item: GetProductResponse.GetProductResponseItem, clickType: String) {
                when (clickType) {
                    "remove" -> {
                        productAdapter?.let { adapter ->
                            adapter.updateFavourite(0, position)
                            val index =  MainActivity.productAdapter?.productList?.indexOfFirst { it.id == item.id }
                            MainActivity.productAdapter?.updateFavourite(0, index?:0)
                            adapter.removeFavourites(item.id?:0)
                            favouritesBinding.noDataFound.isVisible = adapter.productList.isEmpty()
                        }
                        Toast.makeText(this@FavouritesActivity, getString(R.string.removed_from_favourites), Toast.LENGTH_SHORT).show()
                    }
                    "view" -> {
                        val product = ProductItem(
                            id = item.id ?: 0, title = item.title ?: "", price = item.price ?: 0.0, description = item.description ?: "", category = item.category ?: "", image = item.image ?: "", count = item.rating?.count ?: 0, rate = item.rating?.rate ?: 0.0, isFavourite = item.isFavourite, position = position
                        )
                        val intent = Intent(this@FavouritesActivity, ProductViewActivity::class.java).apply {
                            putExtra("PRODUCT", product)
                        }
                        startActivity(intent)
                    }
                }
            }
        })

        val list = MainActivity.productAdapter?.productList?.filter { it.isFavourite == 1 } as ArrayList<GetProductResponse.GetProductResponseItem>
        productAdapter?.updateList(list)

    }

    override fun onResume() {
        super.onResume()
        favouritesBinding.noDataFound.isVisible = productAdapter?.productList?.isEmpty() == true

    }

    private fun setLayoutManager() {
        if (viewType == 0) {
            favouritesBinding.productRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        } else {
            favouritesBinding.productRecycler.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        }
    }

    companion object {
         var productAdapter: ProductAdapter? = null
    }
}
