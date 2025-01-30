package com.example.demo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.demo.R
import com.example.demo.adapter.ProductAdapter
import com.example.demo.databinding.ActivityMainBinding
import com.example.demo.dialog.Loader
import com.example.demo.model.ProductItem
import com.example.demo.response.GetProductResponse
import com.example.demo.utils.Status
import com.example.demo.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val productViewModel: ProductViewModel by viewModels()
    private var viewType = 0
    private val timeInterval = 2000
    private var mBackPressed: Long = 0

    @Inject
    lateinit var loader: Loader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.actionBar.title.text = getString(R.string.home)

        mainBinding.toggleButton.setOnCheckedChangeListener { _, isChecked ->
            viewType = if (isChecked) {
                1
            } else {
                0
            }
            setLayoutManager()
            productAdapter?.updateViewType(viewType)
        }

        mainBinding.favourites.setOnClickListener {
            startActivity(Intent(this, FavouritesActivity::class.java))
        }

        setLayoutManager()
        productAdapter = ProductAdapter()
        mainBinding.productRecycler.adapter = productAdapter
        (mainBinding.productRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        productAdapter?.setRecyclerListener(object : ProductAdapter.RecyclerListener {
            override fun onclick(position: Int, item: GetProductResponse.GetProductResponseItem, clickType: String) {
                when (clickType) {
                    "add" -> {
                        productAdapter?.updateFavourite(1, position)
                        Toast.makeText(this@MainActivity, getString(R.string.added_to_favourites), Toast.LENGTH_SHORT).show()

                    }

                    "remove" -> {
                        productAdapter?.updateFavourite(0, position)
                        Toast.makeText(this@MainActivity, getString(R.string.removed_from_favourites), Toast.LENGTH_SHORT).show()

                    }

                    "view" -> {
                        val product = ProductItem(
                            id = item.id ?: 0, title = item.title ?: "", price = item.price ?: 0.0, description = item.description ?: "", category = item.category ?: "", image = item.image ?: "", count = item.rating?.count ?: 0, rate = item.rating?.rate ?: 0.0, isFavourite = item.isFavourite, position = position
                        )
                        val intent = Intent(this@MainActivity, ProductViewActivity::class.java).apply {
                            putExtra("PRODUCT", product)
                        }
                        startActivity(intent)
                    }

                }
            }
        })

        getProducts()

        onBackPressedDispatcher.addCallback(this) {
            if (mBackPressed + timeInterval > System.currentTimeMillis()) {
                finishAffinity()
            } else Toast.makeText(this@MainActivity,resources.getString(R.string.tap_to_exit),Toast.LENGTH_SHORT).show()

            mBackPressed = System.currentTimeMillis()

        }

    }

    private fun setLayoutManager() {
        if (viewType == 0) {
            mainBinding.productRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        } else {
            mainBinding.productRecycler.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        }
    }

    private fun getProducts() {
        productViewModel.getProducts().observe(this@MainActivity) {
            when (it.status) {
                Status.SUCCESS -> {
                    loader.dismiss()
                    if (it.data.isNullOrEmpty()) {
                        mainBinding.noDataFound.visibility = View.VISIBLE
                    } else {
                        productAdapter?.updateList(it.data)
                    }
                }

                Status.ERROR -> {
                    loader.dismiss()
                    mainBinding.noDataFound.visibility = View.VISIBLE
                    Toast.makeText(this, it.message ?: getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                }

                Status.LOADING -> {
                    loader.show()
                }
            }
        }
    }

    companion object {
        var productAdapter: ProductAdapter? = null

    }

}