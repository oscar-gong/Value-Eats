import styled from 'styled-components';
import { Box } from '@material-ui/core';

export const ReviewInfoBox= styled(Box)`
    display: flex;
    @media (max-width: 800px) {
        flex-direction: column;
    }
`