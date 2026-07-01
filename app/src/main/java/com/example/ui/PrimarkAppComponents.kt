package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Product
import com.example.ui.theme.*

@Composable
fun ProductImagePlaceholder(
    category: String,
    subCategory: String,
    colors: String,
    modifier: Modifier = Modifier
) {
    val primaryColor = when (category) {
        "Women" -> Color(0xFFF472B6)
        "Men" -> Color(0xFF3B82F6)
        "Kids" -> Color(0xFF10B981)
        "Baby" -> Color(0xFF8B5CF6)
        "Home" -> Color(0xFFF59E0B)
        "Accessories" -> Color(0xFF06B6D4)
        "Beauty" -> Color(0xFFEC4899)
        else -> PrimarkBlue
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(primaryColor.copy(alpha = 0.15f), primaryColor.copy(alpha = 0.02f)),
                    radius = 350f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            val w = size.width
            val h = size.height
            val center = Offset(w / 2, h / 2)

            when (subCategory) {
                "Basics", "Outerwear" -> {
                    // Draw a classic flat design t-shirt or jacket
                    val neckRadius = w * 0.12f
                    val shoulderW = w * 0.35f
                    val bodyW = w * 0.55f
                    val bodyH = h * 0.5f

                    // Tee Body path
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x - bodyW / 2, center.y - bodyH / 3),
                        size = Size(bodyW, bodyH),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                    // Left sleeve
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x - bodyW / 2 - w * 0.18f, center.y - bodyH / 3),
                        size = Size(w * 0.2f, bodyH * 0.4f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    // Right sleeve
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x + bodyW / 2 - w * 0.02f, center.y - bodyH / 3),
                        size = Size(w * 0.2f, bodyH * 0.4f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    // Neck cutout
                    drawCircle(
                        color = BgLight,
                        radius = neckRadius,
                        center = Offset(center.x, center.y - bodyH / 3)
                    )
                }
                "Jeans" -> {
                    // Draw stylish folded denim jeans
                    val legW = w * 0.22f
                    val waistW = w * 0.5f
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x - waistW / 2, center.y - h * 0.25f),
                        size = Size(waistW, h * 0.12f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    // Left leg
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x - waistW / 2 + 2f, center.y - h * 0.12f),
                        size = Size(legW, h * 0.45f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    // Right leg
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x + waistW / 2 - legW - 2f, center.y - h * 0.12f),
                        size = Size(legW, h * 0.45f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
                "Footwear" -> {
                    // Draw a classic sneaker silhouette
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x - w * 0.35f, center.y),
                        size = Size(w * 0.7f, h * 0.22f),
                        cornerRadius = CornerRadius(16f, 16f)
                    )
                    // Sneaker ankle/tongue collar
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x - w * 0.2f, center.y - h * 0.2f),
                        size = Size(w * 0.35f, h * 0.22f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                    // Rubber toe bumper (White/Grey)
                    drawRoundRect(
                        color = Color.LightGray,
                        topLeft = Offset(center.x + w * 0.22f, center.y + h * 0.05f),
                        size = Size(w * 0.13f, h * 0.17f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
                "Sleepwear", "Loungewear" -> {
                    // Draw a cute hanger with a pajamas outline
                    val hangerW = w * 0.4f
                    drawArc(
                        color = Color.Gray,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(center.x - hangerW / 2, center.y - h * 0.3f),
                        size = Size(hangerW, h * 0.15f),
                        style = Stroke(width = 4f)
                    )
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x - w * 0.28f, center.y - h * 0.12f),
                        size = Size(w * 0.56f, h * 0.42f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                }
                "Bedding" -> {
                    // Cozy pillow/duvet outline
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x - w * 0.35f, center.y - h * 0.25f),
                        size = Size(w * 0.7f, h * 0.5f),
                        cornerRadius = CornerRadius(20f, 20f)
                    )
                    // Pattern stripes inside
                    drawLine(
                        color = Color.White.copy(alpha = 0.5f),
                        start = Offset(center.x - w * 0.25f, center.y - h * 0.1f),
                        end = Offset(center.x + w * 0.25f, center.y - h * 0.1f),
                        strokeWidth = 6f
                    )
                    drawLine(
                        color = Color.White.copy(alpha = 0.5f),
                        start = Offset(center.x - w * 0.25f, center.y + h * 0.1f),
                        end = Offset(center.x + w * 0.25f, center.y + h * 0.1f),
                        strokeWidth = 6f
                    )
                }
                "Skincare", "Makeup" -> {
                    // Draw a beautiful cosmetics droplet or elegant dropper bottle
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x - w * 0.18f, center.y - h * 0.22f),
                        size = Size(w * 0.36f, h * 0.5f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                    // Bottle cap
                    drawRoundRect(
                        color = Color.DarkGray,
                        topLeft = Offset(center.x - w * 0.12f, center.y - h * 0.35f),
                        size = Size(w * 0.24f, h * 0.15f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
                else -> {
                    // Draw an elegant retail bag placeholder
                    val bagW = w * 0.45f
                    val bagH = h * 0.5f
                    // Bag handles
                    drawCircle(
                        color = primaryColor,
                        radius = bagW * 0.35f,
                        center = Offset(center.x, center.y - bagH / 3),
                        style = Stroke(width = 5f)
                    )
                    // Bag outline
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(center.x - bagW / 2, center.y - bagH / 4),
                        size = Size(bagW, bagH),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                }
            }
        }
    }
}

@Composable
fun PrimarkProductCard(
    product: Product,
    isWishlisted: Boolean,
    onProductClick: () -> Unit,
    onWishlistClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick)
            .testTag("product_card_${product.id}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, BorderLight),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                // Bespoke Vector Graphic Placeholder
                ProductImagePlaceholder(
                    category = product.category,
                    subCategory = product.subCategory,
                    colors = product.colors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Column(modifier = Modifier.padding(12.dp)) {
                    // Subcategory/Category Tag
                    Text(
                        text = "${product.category} • ${product.subCategory}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextLight,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Title
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextDark,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Rating and low price tag
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Low Price emphasis
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "£${"%.2f".format(product.price)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (product.isPromo) PrimarkSaleRed else PrimarkBlueDark,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.testTag("price_tag_${product.id}")
                            )
                            if (product.isPromo) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "£${"%.2f".format(product.originalPrice)}",
                                    style = TextStyle(
                                        color = TextLight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal,
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                )
                            }
                        }

                        // Mini Rating indicator
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Rating",
                                tint = StarGolden,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = product.rating.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextDark,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Promotional Badge overlays
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge: Under 10 or Promo
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (product.isPromo) PrimarkSaleRedLight
                            else if (product.isUnderTen) PrimarkYellowLight
                            else PrimarkBlueLight
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (product.isPromo) "SAVE"
                        else if (product.isUnderTen) "UNDER £10"
                        else if (product.isNewArrival) "NEW"
                        else "TRENDING",
                        color = if (product.isPromo) PrimarkSaleRed
                        else if (product.isUnderTen) Color(0xFFB45309) // Warm amber
                        else PrimarkBlue,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp
                    )
                }

                // Favorite icon button (Wishlist toggle)
                IconButton(
                    onClick = onWishlistClick,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        .testTag("wishlist_btn_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) PrimarkSaleRed else TextLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    onViewAllClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = TextDark
        )
        if (onViewAllClick != null) {
            Text(
                text = "View All",
                style = MaterialTheme.typography.bodyMedium,
                color = PrimarkBlue,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onViewAllClick() }
            )
        }
    }
}
