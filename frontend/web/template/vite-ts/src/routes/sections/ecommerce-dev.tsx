import { lazy } from 'react';
import { Outlet } from 'react-router-dom';

import MainLayout from 'src/layouts/main';
// import AccountLayout from 'src/layouts/account';
import EcommerceDevLayout from 'src/layouts/ecommerce-dev';

// ----------------------------------------------------------------------

// const CartPage = lazy(() => import('src/pages/e-commerce-dev/cart'));
// const ComparePage = lazy(() => import('src/pages/e-commerce-dev/compare'));
// const LandingPage = lazy(() => import('src/pages/e-commerce-dev/landing'));
const ProductPage = lazy(() => import('src/pages/e-commerce-dev/product'));
// const CheckoutPage = lazy(() => import('src/pages/e-commerce-dev/checkout'));
// const ProductsPage = lazy(() => import('src/pages/e-commerce-dev/products'));
// const WishlistPage = lazy(() => import('src/pages/e-commerce-dev/wishlist'));
// const OrderCompletedPage = lazy(() => import('src/pages/e-commerce-dev/order-completed'));

// const AccountOrdersPage = lazy(() => import('src/pages/e-commerce-dev/account/orders'));
// const AccountPaymentPage = lazy(() => import('src/pages/e-commerce-dev/account/payment'));
// const AccountPersonalPage = lazy(() => import('src/pages/e-commerce-dev/account/personal'));
// const AccountVouchersPage = lazy(() => import('src/pages/e-commerce-dev/account/vouchers'));
// const AccountWishlistPage = lazy(() => import('src/pages/e-commerce-dev/account/wishlist'));

// ----------------------------------------------------------------------

export const eCommerceDevRoutes = [
  {
    path: 'dev',
    element: (
      <MainLayout>
        <EcommerceDevLayout>
          <Outlet />
        </EcommerceDevLayout>
      </MainLayout>
    ),
    children: [
      // { element: <LandingPage />, index: true },
      // { path: 'products', element: <ProductsPage /> },
      { path: 'product', element: <ProductPage /> },
      // { path: 'cart', element: <CartPage /> },
      // { path: 'checkout', element: <CheckoutPage /> },
      // { path: 'order-completed', element: <OrderCompletedPage /> },
      // { path: 'wishlist', element: <WishlistPage /> },
      // { path: 'compare', element: <ComparePage /> },
      // {
      //   path: 'account',
      //   element: (
      //     <AccountLayout>
      //       <Outlet />
      //     </AccountLayout>
      //   ),
      //   children: [
      //     { path: 'personal', element: <AccountPersonalPage /> },
      //     { path: 'wishlist', element: <AccountWishlistPage /> },
      //     { path: 'vouchers', element: <AccountVouchersPage /> },
      //     { path: 'orders', element: <AccountOrdersPage /> },
      //     { path: 'payment', element: <AccountPaymentPage /> },
      //   ],
      // },
    ],
  },
];
