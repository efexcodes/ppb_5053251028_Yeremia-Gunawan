package com.example.marketplacesiswa.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marketplacesiswa.ui.screens.CartScreen
import com.example.marketplacesiswa.ui.screens.CheckoutScreen
import com.example.marketplacesiswa.ui.screens.DetailScreen
import com.example.marketplacesiswa.ui.screens.FavoritesScreen
import com.example.marketplacesiswa.ui.screens.HomeScreen
import com.example.marketplacesiswa.ui.screens.OrdersScreen
import com.example.marketplacesiswa.ui.screens.ProfileScreen
import com.example.marketplacesiswa.viewmodel.MarketViewModel

object Routes {
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val ORDERS = "orders"
    const val PROFILE = "profile"
    const val DETAIL = "detail"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
}

private data class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val topLevelDestinations = listOf(
    TopLevelDestination(Routes.HOME, "Beranda", Icons.Filled.Home),
    TopLevelDestination(Routes.FAVORITES, "Favorit", Icons.Filled.Favorite),
    TopLevelDestination(Routes.ORDERS, "Pesanan", Icons.AutoMirrored.Filled.List),
    TopLevelDestination(Routes.PROFILE, "Profil", Icons.Filled.Person)
)

@Composable
fun MarketApp(viewModel: MarketViewModel) {
    val state by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val showNavigation = topLevelDestinations.any { dest ->
        currentDestination?.hierarchy?.any { it.route == dest.route } == true
    }
    // Layout adaptif: layar lebar (tablet / landscape) memakai NavigationRail + grid.
    val wide = LocalConfiguration.current.screenWidthDp >= 600

    val navigateTopLevel: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Row {
        if (showNavigation && wide) {
            NavigationRail {
                topLevelDestinations.forEach { dest ->
                    NavigationRailItem(
                        selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true,
                        onClick = { navigateTopLevel(dest.route) },
                        icon = { Icon(dest.icon, contentDescription = dest.label) },
                        label = { Text(dest.label) }
                    )
                }
            }
        }
        Scaffold(
            modifier = Modifier.weight(1f),
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                if (showNavigation && !wide) {
                    NavigationBar {
                        topLevelDestinations.forEach { dest ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy
                                    ?.any { it.route == dest.route } == true,
                                onClick = { navigateTopLevel(dest.route) },
                                icon = { Icon(dest.icon, contentDescription = dest.label) },
                                label = { Text(dest.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                viewModel = viewModel,
                wide = wide,
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            )
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    viewModel: MarketViewModel,
    wide: Boolean,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                state = state,
                wide = wide,
                onQueryChange = viewModel::onQueryChange,
                onCategorySelected = viewModel::onCategorySelected,
                onToggleFavorite = viewModel::toggleFavorite,
                onOpenDetail = { navController.navigate("${Routes.DETAIL}/$it") },
                onOpenCart = { navController.navigate(Routes.CART) },
                onAddProduct = viewModel::addProduct
            )
        }
        composable(Routes.FAVORITES) {
            FavoritesScreen(
                state = state,
                wide = wide,
                onToggleFavorite = viewModel::toggleFavorite,
                onOpenDetail = { navController.navigate("${Routes.DETAIL}/$it") }
            )
        }
        composable(Routes.ORDERS) { OrdersScreen(state = state) }
        composable(Routes.PROFILE) {
            ProfileScreen(
                state = state,
                onThemeModeChange = viewModel::setThemeMode,
                onDynamicColorChange = viewModel::setDynamicColor
            )
        }
        composable(
            route = "${Routes.DETAIL}/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { entry ->
            val productId = entry.arguments?.getInt("productId") ?: -1
            val product = state.products.firstOrNull { it.id == productId }
            DetailScreen(
                product = product,
                isFavorite = productId in state.favoriteIds,
                onBack = { navController.popBackStack() },
                onToggleFavorite = { viewModel.toggleFavorite(productId) },
                onAddToCart = { quantity -> product?.let { viewModel.addToCart(it, quantity) } },
                onOpenCart = { navController.navigate(Routes.CART) }
            )
        }
        composable(Routes.CART) {
            CartScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onChangeQuantity = viewModel::changeCartQuantity,
                onRemove = viewModel::removeFromCart,
                onCheckout = { navController.navigate(Routes.CHECKOUT) }
            )
        }
        composable(Routes.CHECKOUT) {
            CheckoutScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onPlaceOrder = viewModel::placeOrder,
                onViewOrders = {
                    navController.navigate(Routes.ORDERS) {
                        popUpTo(Routes.HOME)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
