import styled from "styled-components";

export const BannerPhoto = styled.img`
    width: ${props => props.height * 4.8}px;
    max-height: ${props => props.height}px;
    min-height: ${props => props.height}px;
    object-fit: cover;
`;
