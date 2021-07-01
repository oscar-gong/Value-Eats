import styled from 'styled-components';

export const ProfilePhoto= styled.img`
    max-width: ${props => props.size}px;
    min-width: ${props => props.size}px;
    max-height: ${props => props.size}px;
    min-height: ${props => props.size}px;
    border-radius: 50%;
    border: ${props => (props.size / 30)}px solid black;
`