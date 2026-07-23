import Box from "@mui/material/Box";
import LinearProgress, {linearProgressClasses} from "@mui/material/LinearProgress";
import {varAlpha} from "minimal-shared/utils";

export const MainFallback = () => (
    <Box
        sx={{
            display: 'flex',
            flex: '1 1 auto',
            alignItems: 'center',
            justifyContent: 'center',
        }}
    >
        <LinearProgress
            sx={{
                width: 1,
                maxWidth: 320,
                bgcolor: (theme) => varAlpha(theme.vars!.palette.text.primaryChannel, 0.16),
                [`& .${linearProgressClasses.bar}`]: {bgcolor: 'text.primary'},
            }}
        />
    </Box>
);