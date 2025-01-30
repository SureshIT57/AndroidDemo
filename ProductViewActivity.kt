package com.example.demo.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.demo.R
import com.example.demo.databinding.ActivityProductViewBinding
import com.example.demo.model.ProductItem

class ProductViewActivity : AppCompatActivity() {
    private lateinit var productViewBinding: ActivityProductViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_view)
        productViewBinding.actionBar.back.visibility = View.VISIBLE


        productViewBinding.actionBar.back.setOnClickListener {
            finish()
        }
        val productData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("PRODUCT", ProductItem::class.java)
        } else {
            intent.getParcelableExtra("PRODUCT")
        }

        if (productData != null) {
            productViewBinding.productImage.load(productData.image) {
                crossfade(true)
                error(R.drawable.ic_empty_image)
            }
            productViewBinding.category.text = productData.category
            productViewBinding.title.text = productData.title
            productViewBinding.ratingBar.rating = productData.rate.toFloat()
            productViewBinding.ratingRate.text = productData.rate.toString()
            productViewBinding.ratingCount.text = buildString {
                append("(")
                append(productData.count.toString())
                append(")")
            }
            productViewBinding.price.text = productData.price.toString()
            productViewBinding.description.text = productData.description
            productViewBinding.favourite.setImageDrawable(
                ContextCompat.getDrawable(
                    this, if (productData.isFavourite == 1) R.drawable.ic_favourite_filled else R.drawable.ic_favorite_border
                )
            )

            productViewBinding.favourite.setOnClickListener {
                if (productData.isFavourite == 0) {
                    productData.isFavourite = 1
                    productViewBinding.favourite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favourite_filled))
                    MainActivity.productAdapter?.updateFavourite(1, productData.position)
                    FavouritesActivity.productAdapter?.addFavourite(productData)
                    Toast.makeText(this@ProductViewActivity, getString(R.string.added_to_favourites), Toast.LENGTH_SHORT).show()
                } else {
                    productData.isFavourite = 0
                    productViewBinding.favourite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border))
                    MainActivity.productAdapter?.updateFavourite(0, productData.position)
                    FavouritesActivity.productAdapter?.removeFavourites(productData.id)
                    Toast.makeText(this@ProductViewActivity, getString(R.string.removed_from_favourites), Toast.LENGTH_SHORT).show()
                }
            }

            productViewBinding.share.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, productData.title)
                startActivity(Intent.createChooser(intent, "Share Via"))
            }

        }


    }
}