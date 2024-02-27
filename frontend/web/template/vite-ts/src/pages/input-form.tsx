import { Helmet } from 'react-helmet-async';

import InputForm from 'src/sections/_input-form/input-form';

// ----------------------------------------------------------------------

export default function InputFormPage() {
  return (
    <>
      <Helmet>
        <title> 입력폼</title>
      </Helmet>

      <InputForm />
    </>
  );
}
