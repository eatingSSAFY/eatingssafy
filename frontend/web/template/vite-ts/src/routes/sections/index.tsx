import { lazy, Suspense } from 'react';
import { Outlet, Navigate, useRoutes } from 'react-router-dom';

import MainLayout from 'src/layouts/main';
import EcommerceDevLayout from 'src/layouts/ecommerce-dev';

import { SplashScreen } from 'src/components/loading-screen';

import { authRoutes } from './auth';
import { errorRoutes } from './error';
import { commonRoutes } from './common';
import { careerRoutes } from './career';
import { travelRoutes } from './travel';
import { marketingRoutes } from './marketing';
import { eLearningRoutes } from './elearning';
import { eCommerceRoutes } from './ecommerce';
import { eCommerceDevRoutes } from './ecommerce-dev';
import { componentsRoutes } from './components';
import { inputRoutes } from './input-form';

// ----------------------------------------------------------------------

const IndexPage = lazy(() => import('src/pages/e-commerce-dev/product'));
const SupportPage = lazy(() => import('src/pages/support'));

// ----------------------------------------------------------------------

export default function Router() {
  return useRoutes([
    {
      element: (
        <Suspense fallback={<SplashScreen />}>
          <Outlet />
        </Suspense>
      ),
      children: [
        // 메인페이지가 될 거임
        {
          element: (
            <MainLayout>
              <EcommerceDevLayout>
                <IndexPage />
              </EcommerceDevLayout>
            </MainLayout>
          ),
          index: true,
        },

        {
          path: 'support',
          element: (
            <MainLayout>
              <SupportPage />
            </MainLayout>
          ),
        },

        ...marketingRoutes,

        ...travelRoutes,

        ...careerRoutes,

        ...eLearningRoutes,

        ...eCommerceRoutes,

        ...eCommerceDevRoutes,

        ...componentsRoutes,

        ...authRoutes,

        ...errorRoutes,

        ...commonRoutes,

        ...inputRoutes,

        { path: '*', element: <Navigate to="/404" replace /> },
      ],
    },
  ]);
}
