

import FooterAppBar from 'src/components/appbar/footer-appbar';
import { usePathname } from 'src/routes/hooks';

import { useResponsive } from 'src/hooks/use-responsive';


// ----------------------------------------------------------------------

export default function Footer() {
  // const mdUp = useResponsive('up', 'md');

  const pathname = usePathname();

  // const isHome = pathname === '/';

  const simpleFooter = (
    <FooterAppBar/>
  );

  return <footer>{simpleFooter}</footer>;
}