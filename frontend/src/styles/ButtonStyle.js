import styled from 'styled-components';
import { Button } from '@material-ui/core';

export const ButtonStyled = styled(Button)`
    background: linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%);
    color: white;
    width: ${props => props.widthPercentage}%;
`