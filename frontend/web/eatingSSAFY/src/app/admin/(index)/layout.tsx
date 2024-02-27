'use client';

import AdminLayout from 'src/layouts/admin';

// ----------------------------------------------------------------------

type Props = {
  children: React.ReactNode;
};

export default function Layout({ children }: Props) {
  return <AdminLayout disabledSpacing>{children}</AdminLayout>;
}
