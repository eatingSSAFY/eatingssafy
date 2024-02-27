import { useState } from 'react';

import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';
import { ThumbUpOffAlt, ThumbUpAlt } from '@mui/icons-material';
import { addUserPreference } from 'src/api/request';

interface Props {
    foodId: number
}

export default function ThumbsUp({ foodId }: Props) {
    // null, false, true
    const [isLiked, setIsLiked] = useState(true);

    return (
        <Box sx={{ my: 0.5 }}>
            <IconButton
                sx={{ p: 0.3 }}
                aria-label="Like"
                onClick={() => {
                    setIsLiked(!isLiked);
                    // 좋아요 상태일 때 => 삭제요청
                    if (isLiked) {
                        addUserPreference({"foodId":foodId, "value":0});
                    }
                    // 아무것도 아닐 때 => 좋아요 요청
                    else {
                        addUserPreference({"foodId":foodId, "value":1});
                    }
                }}>
                {isLiked ? <ThumbUpAlt color="info" /> : <ThumbUpOffAlt />}
            </IconButton>
        </Box>
    )}