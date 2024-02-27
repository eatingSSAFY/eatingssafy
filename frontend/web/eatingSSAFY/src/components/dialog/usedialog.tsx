import { m, AnimatePresence } from 'framer-motion';

import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import Paper, { PaperProps } from '@mui/material/Paper';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import { useRouter, useSearchParams } from "next/navigation";

// 모달 창 애니메이션 변수 (변경 가능)
import { varBounce } from 'src/components/animate';

// ----------------------------------------------------------------------

// onClose => dialogOpen.onFalse
// onOpen => dialogOpen.onTrue
// open => dialog.value

interface Props {
  isRegister: boolean,
  is400Error: boolean,
  openDialog: boolean,
  setOpenDialog: (arg0: boolean) => void,
  setis400Error: (arg0: boolean) => void,
}

export default function UseDialog({ isRegister, is400Error, openDialog, setOpenDialog, setis400Error }: Props) {
  const router = useRouter()

  return (
    <>
      <AnimatePresence>
        {openDialog && (
          <Dialog
            // fullWidth={false}
            fullScreen
            // maxWidth={false}
            open={openDialog}
            onClose={() => setOpenDialog(false)}
            PaperComponent={(props: PaperProps) => (
              // <m.div {...getVariant(selectVariant)}>
              <m.div {...varBounce().in}>
                <Paper {...props}>{props.children}</Paper>
              </m.div>
            )}
          >
            {is400Error ? (
              <>
                <DialogTitle id="alert-dialog-title">이름을 한국어로 입력하세요.</DialogTitle>
                <DialogActions>
                  <Button onClick={() => {
                    setOpenDialog(false)
                    setis400Error(false)}}>확인</Button>
                </DialogActions>
              </>
            ) : (
              <>
                <DialogTitle id="alert-dialog-title">제출되었습니다 ^~^</DialogTitle>
                <DialogActions>
                  {!isRegister ? (
                    <Button onClick={() => setOpenDialog(false)}>확인</Button>
                  ) : (
                    <Button onClick={() => {
                                                    setOpenDialog(false);
                                                    router.push('/');}}>확인</Button>
                  )}
                </DialogActions>
              </>
            )}
          </Dialog>
        )}
      </AnimatePresence>
    </>
  );
}