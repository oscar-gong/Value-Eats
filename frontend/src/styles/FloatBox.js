import styled from 'styled-components';
import { Box } from '@material-ui/core';

export const FloatBox = styled(Box)`
    background-color: white;
    border-radius: 20px;
    box-shadow: 5px 7px rgba(125, 125, 125, 20);
    
    min-width: 30%;
    max-width: 40vw !important;
    min-height: 70%;
    max-height: 60vh;
    align-items: center;
`