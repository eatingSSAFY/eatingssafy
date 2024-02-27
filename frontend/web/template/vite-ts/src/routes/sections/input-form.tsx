import { lazy } from 'react';

// ----------------------------------------------------------------------

const InputFormPage = lazy(() => import('src/pages/input-form'));
// ----------------------------------------------------------------------

export const inputRoutes = [
  { path: 'input', element: <InputFormPage /> },
];
