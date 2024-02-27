import { useTheme } from '@mui/material/styles';

import * as Yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import { useState } from 'react';

// 사용자 확인용 모달 창
import SubmitButton from './submit-button';
import UseDialog from 'src/components/dialog/usedialog';

import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import FormProvider, { RHFTextField } from 'src/components/hook-form';
import { addNoCardPerson } from 'src/api/request';

// ----------------------------------------------------------------------


export default function NocardInputHero() {
    const theme = useTheme();
    const [is400Error, setis400Error] = useState<boolean>(false);
    const [openDialog, setOpenDialog] = useState<boolean>(false);

    const isRegister : boolean = false;


    const validationSchema = Yup.object().shape({
        // 이름 한글로만 입력
        personName: Yup.string()
            .required('필수 입력 값입니다.'),

        // 컨설턴트의 경우, '컨설턴트 ㅇㅇㅇ'으로 입력함
        personId: Yup.string()
            .required('필수 입력 값입니다.')
            .min(4, '최소 4자리 이상')
            .max(10, '최대 10자리 이하'), // 7자리 이상 ~ ??자리 이하
    });

    // 기본 값, 리셋 시 기본 값으로 리셋됨
    const defaultValues = {
        personName: '',
        personId: '',
    };


    const methods = useForm({
        resolver: yupResolver(validationSchema),
        defaultValues,
        mode: 'onChange'
    });

    const {
        // 제출 완료 시 입력 폼 내부 값 리셋
        reset,
        // 제출 함수
        handleSubmit,
        // 변수 값 관찰
        watch,
        // 유효성 검사
        formState: { isValid, isSubmitting }
    } = methods;

    const onSubmit = handleSubmit(async (data: object) => {
        try {
            await new Promise((resolve) => setTimeout(resolve, 500));
            reset();
            addNoCardPerson(data)
                .then(() => { setOpenDialog(true) })
                .catch(error => {
                    // 다른 에러가 있을까요?
                    if (error.request.status === 400) {
                        setis400Error(true)
                    }
                    setOpenDialog(true)
                })
        } catch (error) {
            console.error(error);
        }
    });

    // 'personName'과 'personId' 필드의 현재 값을 관찰합니다.
    // 값을 자식 컴포넌트로 전달하기 위해 정의한 변수
    const personName = watch('personName');
    const personId = watch('personId');

    const renderForm = (
        <FormProvider methods={methods} onSubmit={onSubmit}>
            <Typography variant="h5" sx={{ mb: 3 }}>
                카드 미소지자 입력폼
            </Typography>
            <Stack spacing={2.5} alignItems="flex-end">
                <RHFTextField name="personName" label="이름" />
                <RHFTextField name="personId" label="학번/사번" />
                {/* 모달 창으로 onSubmit 함수와 입력 값 전달 */}
                <SubmitButton
                    valid={isValid}
                    onSubmit={onSubmit}
                    personName={personName}
                    personId={personId}
                />
            </Stack>
            {openDialog &&
                <UseDialog
                    isRegister={isRegister}
                    is400Error={is400Error}
                    openDialog={openDialog}
                    setOpenDialog={setOpenDialog}
                    setis400Error={setis400Error}
                ></UseDialog>
            }
        </FormProvider>
    );

    return (
        <>
            {renderForm}
        </>
    );
}
