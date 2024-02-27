import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';

import { usePathname } from 'src/routes/hooks';

import { useResponsive } from 'src/hooks/use-responsive';

import Logo from 'src/components/logo';

// ----------------------------------------------------------------------

export default function Footer() {
  const mdUp = useResponsive('up', 'md');

  const pathname = usePathname();

  const isHome = pathname === '/';

  const simpleFooter = (
    <Container sx={{ py: 8, textAlign: 'center' }}>
      {/* TODO: eatingSSAFY로 변경 */}
      <Logo single />

      <Typography variant="caption" component="div" sx={{ color: 'text.secondary' }}>
        © 2024. All rights reserved
      </Typography>
    </Container>
  );

  return <footer>{simpleFooter}</footer>;
}