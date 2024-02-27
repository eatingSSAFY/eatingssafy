import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';

import { floors, monthIsOver10, dateIsOver10 } from './menu-constants';
import Iconify from 'src/components/iconify';

type Props = {
    dates: string[];
    dateState: string;
    floorState: string;
    viewMode: string;
    handleDateChange: (newDate: string) => void;
    handleFloorChange: (newFloor: string) => void;
    handleChangeViewMode: (
        event: React.MouseEvent<HTMLElement>,
        newAlignment: string | null
    ) => void;
}

const VIEW_OPTIONS = [
    { value: 'carousel', icon: <Iconify icon="carbon:carousel-horizontal" /> },
    { value: 'list', icon: <Iconify icon="carbon:list-boxes" /> },
  ];

export default function MenuHeader({ dates, dateState, floorState, viewMode, handleDateChange, handleFloorChange, handleChangeViewMode }: Props) {

    return (
        <Box sx={{ 
            pt:0.5
        }}>
            <Stack>
                <ToggleButtonGroup
                    exclusive
                    size="small"
                    value={dateState}
                    sx={{
                        backgroundColor: '#00000000',
                        border: 'none'
                    }}
                    onChange={(event, newValue) => {
                        if (newValue !== null) {
                            handleDateChange(newValue);
                        }
                    }}
                >
                    {dates.map((dates, index) => (
                        <ToggleButton key={dates} value={dates} sx={{ width: 1 }}>
                            {/* "2024-01-30화" => "01/30 화" */}
                            {dates.slice(6-monthIsOver10,7)}/{dates.slice(9-dateIsOver10,10)} <br/>
                            {dates.slice(-1)} 
                        </ToggleButton>
                    ))}
                </ToggleButtonGroup>
                <Box sx={{display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                    <ToggleButtonGroup
                        exclusive
                        size="small"
                        value={floorState}
                        sx={{
                            backgroundColor: '#00000000',
                            border: 'none',
                            width:0.5
                        }}
                        onChange={(event, newValue) => {
                            if (newValue !== null) {
                                handleFloorChange(newValue);
                            }
                        }}
                    >
                        {floors.map((floor, index) => (
                            <ToggleButton key={floor} value={floor} sx={{width: 0.5}}>
                                {floor}
                            </ToggleButton>
                        ))}
                    </ToggleButtonGroup>
                    <ToggleButtonGroup
                        exclusive
                        size="small"
                        value={viewMode}
                        onChange={handleChangeViewMode}
                        sx={{
                            backgroundColor: '#00000000',
                            border: 'none'
                        }}
                        >
                        {VIEW_OPTIONS.map((option) => (
                            <ToggleButton key={option.value} value={option.value}>
                                {option.icon}
                            </ToggleButton>
                        ))}
                    </ToggleButtonGroup>
                </Box>
            </Stack>
        </Box>
    );
}