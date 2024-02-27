import { m, AnimatePresence } from 'framer-motion';

import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import Paper, { PaperProps } from '@mui/material/Paper';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';

// 모달 창 애니메이션 변수 (변경 가능)
import { varBounce } from 'src/components/animate';
import { useBoolean } from 'src/hooks/use-boolean';

// ----------------------------------------------------------------------

// onClose => dialogOpen.onFalse
// onOpen => dialogOpen.onTrue
// open => dialog.value

// Props
interface SubmitButtonProps {
  onSubmit: () => void;
  valid: boolean;
  personName: string;
  personId: string;
}

export default function SubmitButton({ onSubmit, personName, personId, valid }: SubmitButtonProps) {
  const dialogOpen = useBoolean();

  const handleConfirm = () => {
    dialogOpen.onFalse();
    onSubmit(); // 부모 컴포넌트에서 전달받은 onSubmit 함수 호출
  };

  return (
    <>
      <Button fullWidth size="large" variant="contained" onClick={() => { dialogOpen.onTrue(); }}
      >
        제출
      </Button>
      <AnimatePresence>
        {dialogOpen.value && (
          <Dialog
            // fullWidth={false}
            fullScreen
            // maxWidth={false}
            open={dialogOpen.value}

            onClose={dialogOpen.onFalse}
            PaperComponent={(props: PaperProps) => (
              // <m.div {...getVariant(selectVariant)}>
              <m.div {...varBounce().in}>
                <Paper {...props}>{props.children}</Paper>
              </m.div>
            )}
          >
            {valid ? (
              <>
                <DialogTitle id="alert-dialog-title">{`제출하시겠습니까?`}</DialogTitle>

                <DialogContent>
                  {`이름 : ${personName}`} <br />
                  {`학번/사번 : ${personId}`}
                </DialogContent>

                <DialogActions>
                  {/* 제출하지 않으면 내용 그대로 */}
                  <Button onClick={dialogOpen.onFalse}>아니오</Button>
                  {/* 제출하면 내용 초기화 */}
                  <Button variant="contained" onClick={handleConfirm} autoFocus>
                    예
                  </Button>
                </DialogActions>
              </>
            ) : (
              <>
                <DialogTitle id="alert-dialog-title">다시 입력하세요.</DialogTitle>
                <DialogActions>
                  <Button onClick={dialogOpen.onFalse}>확인</Button>
                </DialogActions>
              </>
            )}
          </Dialog>
        )}
      </AnimatePresence>
    </>
  );
}