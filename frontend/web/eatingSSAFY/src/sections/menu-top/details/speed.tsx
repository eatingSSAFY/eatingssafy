import Box from '@mui/material/Box';
import lottie, { AnimationItem } from 'lottie-web';
import { useEffect, useRef } from 'react';

interface Props {
    velocity: number
}

export default function Speed({velocity} : Props) {
    const likecontainer = useRef();

    useEffect(() => {

        let animation : AnimationItem;

        if (likecontainer.current) {
            animation = lottie.loadAnimation({
                container: likecontainer.current,
                renderer: 'svg',
                loop: true,
                autoplay: true,
                animationData: require("public/assets/gif/fire.json")
            });

            animation.setSpeed(velocity);
        }

        return () => {
            if (animation) {
              animation.destroy();
            }
          };
    }, [velocity])

    return (
        <Box ref={likecontainer} sx={{height: '28px' }}>
        </Box>
    )
}
