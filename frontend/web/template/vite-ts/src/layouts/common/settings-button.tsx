// 다크모드 ,,,,

import { m } from 'framer-motion';

import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';
import { Theme, SxProps } from '@mui/material/styles';
import Badge, { badgeClasses } from '@mui/material/Badge';

import Iconify from 'src/components/iconify';
import { Switch } from '@mui/material';
import { varHover } from 'src/components/animate';
import { useSettingsContext } from 'src/components/settings';

import BaseOptions from 'src/components/settings/drawer/base-options';

// ----------------------------------------------------------------------

type Props = {
  sx?: SxProps<Theme>;
};

export default function SettingsButton({ sx }: Props) {
  const settings = useSettingsContext();

  const renderMode = (
    <BaseOptions
      title="Mode"
      selected={settings.themeMode === 'dark'}
      onClick={() =>
        settings.onUpdate('themeMode', settings.themeMode === 'dark' ? 'light' : 'dark')
      }
      icons={['carbon:asleep', 'carbon:asleep-filled']}
    />
  );


  return (
    <Badge
      // color="error"
      // variant="dot"
      // invisible={!settings.canReset}
      // sx={{
      //   [`& .${badgeClasses.badge}`]: {
      //     top: 8,
      //     right: 8,
      //   },
      //   ...sx,
      // }}
    >
      <Box
        // sx={{ pt: 2 }}
        component={m.div}
        // animate={{
        //   rotate: [0, settings.open ? 0 : 360],
        // }}
        transition={{
          duration: 12,
          ease: 'linear',
          repeat: Infinity,
        }}
      >
        {/* <IconButton
          component={m.button}
          whileTap="tap"
          whileHover="hover"
          variants={varHover(1.05)}
          color="inherit"
          aria-label="settings"
          onClick={settings.onToggle}
        >
          <Iconify icon="carbon:settings" />
        </IconButton> */}
        {renderMode}
      </Box>
    </Badge>
  );
}
